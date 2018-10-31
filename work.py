import os
from nltk.stem import PorterStemmer, WordNetLemmatizer
from nltk import word_tokenize, pos_tag
from nltk.corpus import stopwords, wordnet

def get_wordnet_pos(treebank_tag):
    if treebank_tag.startswith('J'):
        return wordnet.ADJ
    elif treebank_tag.startswith('V'):
        return wordnet.VERB
    elif treebank_tag.startswith('N'):
        return wordnet.NOUN
    elif treebank_tag.startswith('R'):
        return wordnet.ADV
    else:
        return wordnet.NOUN

def preproccess(text):
    text_list = word_tokenize(text)

    english_punctuations = [',', '.', ':', ';', '?', '(', ')', '[', ']', '&', '!', '*', '@', '#', '$', '%']
    text_list = [word for word in text_list if word not in english_punctuations]
    stops = set(stopwords.words("english"))
    text_list = [word for word in text_list if word not in stops]
    return text_list

def stem(s, filename):
    stemmer = PorterStemmer()
    with open("result/{}".format(filename), "w") as f:
        f.write(" ".join([stemmer.stem(word) for word in s]))
        f.close()


def lemmatize(s, filename):
    lemmatizer = WordNetLemmatizer()
    result = []
    with open("result/{}".format(filename), "w") as f:
        for word, tag in pos_tag(s):
            result.append(lemmatizer.lemmatize(word.lower(), get_wordnet_pos(tag)))
        f.write(" ".join(result))
        f.close()


def main():
    if not os.path.exists("result"):
        os.mkdir("result")
    with open("data/1.txt", "r") as f:
        text1 = preproccess(f.read())
        f.close()
    with open("data/2.txt", "r") as f:
        text2 = preproccess(f.read())
        f.close()
    stem(text1, "1_stem.txt")
    stem(text2, "2_stem.txt")
    lemmatize(text1, "1_lemmatize.txt")
    lemmatize(text2, "2_lemmatize.txt")


if __name__ == "__main__":
    main()
